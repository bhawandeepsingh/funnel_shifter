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
		wp := wp + 1.U // Check 1 - + is expected to truncate
	}

	when (io.pull && !(io.empty))
	{
		io.data_out := mem(rp)
		rp := rp + 1.U // Check 2 - + is expected to truncate
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


