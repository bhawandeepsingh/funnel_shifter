import chisel3._
import chisel3.util._
//import chiseltest._
//import chisel3.tester.RawTester.test

class sync_fifo (mem_size : Int, in_word_size : Int) extends Module  
{
	val io = IO ( new Bundle 
	{
		val push = Input (Bool())
		val pull = Input (Bool())
		val data_in = Input(UInt(in_word_size.W)) 
		val data_out = Output(UInt(in_word_size.W)) 
		val full = Output(Bool())
		val empty = Output(Bool())
        
        // temp - to be removed
        val rp_port = Output(UInt((log2Ceil(mem_size) +1).W))
        val wp_port = Output(UInt((log2Ceil(mem_size) +1).W))
        // temp over
	})

	val wp = RegInit(0.U((log2Ceil(mem_size) +1).W))
	val rp = RegInit(0.U((log2Ceil(mem_size)+1).W))

	val mem = Mem(mem_size, UInt(in_word_size.W))

	//wp := 0.U
	//rp := 0.U
    // temp - to be removed
    io.rp_port := rp
    io.wp_port := wp
    // temp over
    
    io.data_out := 0.U
	//full = 0.U
	//empty = 1.U

	when(io.push && !(io.full))
	{
		mem(wp) := io.data_in
		when ( (wp(log2Ceil(mem_size)-1, 0) +& 1.U) <= (mem_size -1).U)
        {
            wp := wp + 1.U
        }
        .otherwise
        {
			wp := Cat( ~wp(log2Ceil(mem_size)), 0.U((log2Ceil(mem_size)).W))
        }
	}

	when (io.pull && !(io.empty))
	{
		io.data_out := mem(rp)
		when ( (rp(log2Ceil(mem_size)-1, 0) +& 1.U) <= (mem_size -1).U)
        {
            rp := rp + 1.U
        }
        .otherwise
        {
			rp := Cat( ~rp(log2Ceil(mem_size)), 0.U((log2Ceil(mem_size)).W))
        }
	}

	when (wp === rp)
	{
		io.empty := 1.U
	}
	.otherwise
	{
		io.empty := 0.U
	}

	when ( ( wp(log2Ceil(mem_size)) =/= rp(log2Ceil(mem_size)) ) && ( wp(log2Ceil(mem_size)-1, 0) === rp(log2Ceil(mem_size)-1, 0) ) )
	{
		io.full := 1.U
	}
	.otherwise
	{
		io.full := 0.U
	}

}



class funnel_shifter (mem_size : Int, in_word_size : Int, out_word_size : Int, buffer_size : Int) extends Module
{
    val io = IO (new Bundle { 
        val push = Input (Bool())
		val pull = Input (Bool())
		val data_in = Input(UInt(in_word_size.W)) 
		val data_out = Output(UInt(out_word_size.W)) 
		val full = Output(Bool())
		val empty = Output(Bool())
        
        // temp 
        val mem_rp_port = Output(UInt((log2Ceil(mem_size) + 1).W))
        val mem_wp_port = Output(UInt((log2Ceil(mem_size) + 1).W))
        val buffer_wp_port = Output (UInt(log2Ceil(buffer_size).W))
        val buffer_rp_port = Output (UInt(log2Ceil(buffer_size).W))
		val free_entries_port = Output (UInt(log2Ceil(buffer_size).W))

    })
    require(buffer_size >= out_word_size)
    require(buffer_size % in_word_size == 0)
    
    io.data_out := 0.U
    
    val fifo_inst = Module(new sync_fifo(mem_size, in_word_size))
    fifo_inst.io.push := io.push
    fifo_inst.io.data_in := io.data_in
    io.full := fifo_inst.io.full

    
    //val buffer_size = in_word_size*4  // Can parameterize 4 if needed
    val buffer = RegInit(0.U(buffer_size.W)) 

    val free_entries = RegInit (buffer_size.U(log2Ceil(buffer_size).W))
    //val filled_entries = RegInit (0.U(log2Ceil(buffer_size).W))
    val buffer_wp = RegInit (0.U(log2Ceil(buffer_size).W))
    val buffer_rp = RegInit (0.U(log2Ceil(buffer_size).W))
    
    //temp
    io.mem_wp_port := fifo_inst.io.wp_port
    io.mem_rp_port := fifo_inst.io.rp_port
    io.buffer_wp_port := buffer_wp
    io.buffer_rp_port := buffer_rp 
	io.free_entries_port := free_entries
    //temp over
    
    when ( (buffer_size.U - free_entries) < out_word_size.U)
    {
        io.empty := 1.U
    }
    .otherwise
    {
        io.empty := 0.U
    }
    
    when ( (free_entries >= in_word_size.U) && !(fifo_inst.io.empty) ) // Do not assert pull if sync_fifo is empty
    {
        fifo_inst.io.pull := 1.U
    }
	.elsewhen (  (free_entries + out_word_size.U >= in_word_size.U) && io.pull  && !(fifo_inst.io.empty) )
	{
		fifo_inst.io.pull := 1.U
	}
    .otherwise
    {
        fifo_inst.io.pull := 0.U
    }
    
    // rp
    when (io.pull && !(io.empty))
    {
        //buffer_rp := buffer_rp + out_word_size.U
        when ( (buffer_rp +& out_word_size.U) <= (buffer_size -1).U)
        {
            buffer_rp := buffer_rp + out_word_size.U
        }
        .otherwise
        {
            buffer_rp := buffer_rp + out_word_size.U - buffer_size.U
        }
    }
    
    // wp 
    when (fifo_inst.io.pull)
    {
        when ( (buffer_wp +& in_word_size.U) <= (buffer_size -1).U)
        {
            buffer_wp := buffer_wp + in_word_size.U
        }
        .otherwise
        {
            buffer_wp := buffer_wp + in_word_size.U - buffer_size.U
        }
    }
    
    
    // buffer_update - handle wraparound
    // buffer_input wire
    //val buffer_in = Wire(UInt(buffer_size.W))
    //buffer_in := buffer
    when (fifo_inst.io.pull)
    {
        //when ((buffer_wp +& in_word_size.U - 1.U) <= (buffer_size-1).U ) // No wraparound
        //{
            buffer := ((buffer >> (buffer_wp + in_word_size.U)) << (buffer_wp + in_word_size.U)) | (fifo_inst.io.data_out << buffer_wp) | ((buffer << (buffer_size.U - buffer_wp)) >> (buffer_size.U - buffer_wp))
        //}
        //.otherwise // Wraparound
        //{
            
        //}
    }
    
    // buffer register
    /*when (fifo_inst.io.pull)
    {
            buffer := buffer_in
    }*/
    
    // free_entries
    when ( fifo_inst.io.pull && (io.pull && !(io.empty) ) )
    {
        free_entries := free_entries - in_word_size.U + out_word_size.U
    }
    .elsewhen (fifo_inst.io.pull)
    {
        free_entries := free_entries - in_word_size.U
    }
    .elsewhen (io.pull && !(io.empty))
    {
        free_entries := free_entries + out_word_size.U
    }
       
    // data_out - handle wraparound
    when (io.pull && !(io.empty))
    {
        when ((buffer_rp +& out_word_size.U) <= (buffer_size-1).U ) // No wraparound
        {
            io.data_out := (buffer >> buffer_rp)(out_word_size - 1, 0)
        }
        .otherwise // Wraparound
        {
            io.data_out := (buffer >> buffer_rp)(out_word_size - 1, 0) | (buffer << (buffer_size.U - buffer_rp))(out_word_size - 1, 0)
            /*((buffer << ((buffer_size.U << 1.U) - out_word_size.U - buffer_rp)) >> (1.U))(out_word_size - 1, 0)*/
        }
    }
}



