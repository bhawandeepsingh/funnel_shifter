import chisel3._
import chisel3.util._
import chiseltest._
import chisel3.tester.RawTester.test


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




def test_sync_fifo: Boolean = {
    test(new sync_fifo(4, 32)) { c =>
        //for (i <- 0 until 16) {
        //    for (j <- 0 until 16) {
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.push.poke(1.B)
                c.io.data_in.poke(20.U)
        
                c.clock.step(1)
                c.io.empty.expect(0.B)
        
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.pull.poke(1.B)
        
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.full.expect(0.B)
                
                c.io.push.poke(0.B)
        
                c.io.pull.poke(1.B)
                c.io.data_out.expect(20.U)
                //c.io.data_out.peek()
                //println(s\"s data_out:${c.io.data_out.peek()} \")\n"
                //println(s" data_out:${c.io.data_out.peek()}")
        
        
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
                c.io.full.expect(0.B)
        
                c.io.data_out.expect(20.U)
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.data_out.expect(20.U)
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                //c.io.data_out.expect(20.U)
                c.io.empty.expect(1.B)
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                
        //    }
       // }
    }
    println(getVerilog(new sync_fifo(4, 32)))
    true
}

assert(test_sync_fifo)
