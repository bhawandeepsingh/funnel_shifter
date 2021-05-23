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
    })
    
    io.data_out := 0.U
    
    val fifo_inst = Module(new sync_fifo(mem_size, in_word_size))
    fifo_inst.io.push := io.push
    fifo_inst.io.data_in := io.data_in
    io.full := fifo_inst.io.full
    io.mem_wp_port := fifo_inst.io.wp_port
    io.mem_rp_port := fifo_inst.io.rp_port
    
    //val buffer_size = in_word_size*4  // Can parameterize 4 if needed
    val buffer = RegInit(0.U(buffer_size.W)) 
    val free_entries = RegInit (buffer_size.U(log2Ceil(buffer_size).W))
    //val filled_entries = RegInit (0.U(log2Ceil(buffer_size).W))
    val buffer_wp = RegInit (0.U(log2Ceil(buffer_size).W))
    val buffer_rp = RegInit (0.U(log2Ceil(buffer_size).W))
    
    //temp
    io.buffer_wp_port := buffer_wp
    io.buffer_rp_port := buffer_rp 
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
    when (fifo_inst.io.pull)
    {
        when ((buffer_wp +& in_word_size.U) <= (buffer_size-1).U ) // No wraparound
        {
            //buffer(buffer_wp + in_word_size.U - 1.U, buffer_wp) := fifo_inst.io.data_out
        }
        .otherwise // Wraparound
        {
            
        }
    }
    
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




def test_funnel_shifter: Boolean = {
    test(new funnel_shifter(4, 21, 9, 42)) { c =>
        //for (i <- 0 until 16) {
        //    for (j <- 0 until 16) {
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.push.poke(1.B)
                c.io.data_in.poke(1431655765.U)
        
                c.clock.step(1) // CC 1
                
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" ***************************** ")
        
                c.clock.step(1)  // CC 2
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
                c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
        
                c.clock.step(1) // CC 3
				c.io.push.poke(0.B)
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.pull.poke(1.B)
        
                c.clock.step(1) // CC 4
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.full.expect(0.B)
                
                c.io.push.poke(0.B)
        
                //c.io.pull.poke(1.B)
                //c.io.data_out.expect(20.U)
                println(s" data_out:${c.io.data_out.peek()}")
                //c.io.data_out.peek()
                //println(s\"s data_out:${c.io.data_out.peek()} \")\n"
                //println(s" data_out:${c.io.data_out.peek()}")
        
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.clock.step(1)  // CC 5
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
                c.io.full.expect(0.B)
        
                //c.io.data_out.expect(20.U)
                c.clock.step(1)  // CC 6
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                //c.io.data_out.expect(20.U)
                c.clock.step(1)  // CC 7
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                //c.io.data_out.expect(20.U)
                //c.io.empty.expect(1.B)
                c.clock.step(1)  // CC 8
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")


                 c.clock.step(1)  // CC 9
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")

                c.clock.step(1)  // CC 10
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
				c.io.empty.expect(1.B)

       
                
        //    }
       // }
    }
    println(getVerilog(new funnel_shifter(4, 21, 9, 42)))
    true
}

assert(test_funnel_shifter)

