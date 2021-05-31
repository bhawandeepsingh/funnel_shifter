/*class CombLogic extends Module {
    val io = IO(new Bundle {
        val a   = Input(Bool())
        val b   = Input(Bool())
        val c   = Input(Bool())
        val out = Output(Bool())
    })
    
    // YOUR CODE HERE
    io.out := (io.a & io.b) | (~io.c)
    
    // We can print state like this everytime `step()` is called in our test
    printf(p"a: ${io.a}, b: ${io.b}, c: ${io.c}, out: ${io.out}\n")
}*/


class funnel_shifter (mem_size : Int, in_word_size : Int, out_word_size : Int, buffer_size : Int) extends Module
{
    val io = IO (new Bundle { 
        //val push = Input (Bool())
		//val pull = Input (Bool())
		//val data_in = Input(UInt(in_word_size.W)) 
		//val data_out = Output(UInt(out_word_size.W)) 
		//val full = Output(Bool())
		//val empty = Output(Bool())
		val in = Flipped(Decoupled (UInt(in_word_size.W)))
		val out = Decoupled (UInt(out_word_size.W))
        
        // temp 
        val mem_rp_port = Output(UInt((log2Ceil(mem_size) + 1).W))
        val mem_wp_port = Output(UInt((log2Ceil(mem_size) + 1).W))
        val buffer_wp_port = Output (UInt(log2Ceil(buffer_size).W))
        val buffer_rp_port = Output (UInt(log2Ceil(buffer_size).W))
		val free_entries_port = Output (UInt(log2Ceil(buffer_size).W))

    })
    require(buffer_size >= out_word_size)
    require(buffer_size % in_word_size == 0)

	val push = Wire (Bool())
	val data_in = Wire(UInt(in_word_size.W)) 
	val full = Wire(UInt(1.W))
	push := io.in.valid
	data_in := io.in.bits
	io.in.ready := ~full

	val pull = Wire (Bool())
	val data_out = Wire(UInt(out_word_size.W)) 
	val empty = Wire(UInt(1.W))
	pull := io.out.ready
	io.out.bits := data_out
	io.out.valid := ~empty
    
    data_out := 0.U
    
    val fifo_inst = Module(new sync_fifo(mem_size, in_word_size))
    fifo_inst.io.push := push
    fifo_inst.io.data_in := data_in
    full := fifo_inst.io.full

    
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
        empty := 1.U
    }
    .otherwise
    {
        empty := 0.U
    }
    
    when ( (free_entries >= in_word_size.U) && !(fifo_inst.io.empty) ) // Do not assert pull if sync_fifo is empty
    {
        fifo_inst.io.pull := 1.U
    }
	.elsewhen (  (free_entries + out_word_size.U >= in_word_size.U) && pull  && !(fifo_inst.io.empty) )
	{
		fifo_inst.io.pull := 1.U
	}
    .otherwise
    {
        fifo_inst.io.pull := 0.U
    }
    
    // rp
    when (pull && !(empty))
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
    when ( fifo_inst.io.pull && (pull && !(empty) ) )
    {
        free_entries := free_entries - in_word_size.U + out_word_size.U
    }
    .elsewhen (fifo_inst.io.pull)
    {
        free_entries := free_entries - in_word_size.U
    }
    .elsewhen (pull && !(empty))
    {
        free_entries := free_entries + out_word_size.U
    }
       
    // data_out - handle wraparound
    when (pull && !(empty))
    {
        when ((buffer_rp +& out_word_size.U) <= (buffer_size-1).U ) // No wraparound
        {
            data_out := (buffer >> buffer_rp)(out_word_size - 1, 0)
        }
        .otherwise // Wraparound
        {
            data_out := (buffer >> buffer_rp)(out_word_size - 1, 0) | (buffer << (buffer_size.U - buffer_rp))(out_word_size - 1, 0)
            /*((buffer << ((buffer_size.U << 1.U) - out_word_size.U - buffer_rp)) >> (1.U))(out_word_size - 1, 0)*/
        }
    }
}





def test_funnel_shifter: Boolean = {
    test(new funnel_shifter(4, 21, 9, 42)) { c =>
        //for (i <- 0 until 16) {
        //    for (j <- 0 until 16) {
                                c.io.in.ready.expect(1.B)
                c.io.out.valid.expect(0.B)

                println(s" CC 0  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
                
                c.io.in.valid.poke(1.B)
                c.io.in.bits.poke(1398101.U)
        
                println(s" Poked in.valid and data ")
        
                println(s" ***************************** ")        
                c.clock.step(1) // CC 1
                println(s" CC 1  ")
                
                println(s" Data written to sync_fifo but not to buffer")    
                //c.io.in.valid.poke(1.B)
                //c.io.in.bits.poke(1398101.U)
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
                c.io.in.ready.expect(1.B)
                c.io.out.valid.expect(0.B)
        
                println(s" ***************************** ")        
                c.clock.step(1)  // CC 2
                println(s" CC 2  ")
        
                println(s" Data written to buffer") 
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                println(s" Pull not asserted yet")
                println(s" out.bits:${c.io.out.bits.peek()}")
        
                c.io.out.valid.expect(1.B)
				c.io.in.ready.expect(1.B)
        
                println(s" ***************************** ")
                c.clock.step(1) // CC 3
                println(s" CC 3  ")
                c.io.in.valid.poke(0.B)
                println(s" Removed in.valid ")        
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                c.io.out.ready.poke(1.B)
                println(s" Pull asserted ")
                println(s" out.bits:${c.io.out.bits.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1) // CC 4
                println(s" CC 4  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                c.io.in.ready.expect(1.B)
  
                println(s" ***************************** ")
                c.clock.step(1)  // CC 5
                println(s" CC 5  ")
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")

                c.io.in.ready.expect(1.B)
  
                println(s" ***************************** ")
                c.clock.step(1)  // CC 6
                println(s" CC 6  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 7
                println(s" CC 7  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 8
                println(s" CC 8  ")
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 9
                println(s" CC 9  ")
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 10
                println(s" CC 10  ")
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" out.bits:${c.io.out.bits.peek()}")
                println(s" in.ready:${c.io.in.ready.peek()}")
                println(s" out.valid:${c.io.out.valid.peek()}")
                
				c.io.out.valid.expect(0.B)
                println(s" *Test still has out.ready asserted, out.valid should be asserted")
                println(s" ***************************** ")                         
        //    }
       // }
    }
    println(getVerilog(new funnel_shifter(4, 21, 9, 42)))
    true
}

assert(test_funnel_shifter)

