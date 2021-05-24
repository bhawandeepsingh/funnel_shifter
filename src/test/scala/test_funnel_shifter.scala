import chisel3._
import chisel3.tester._
import org.scalatest._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._



def test_funnel_shifter: Boolean = {
    test(new funnel_shifter(4, 21, 9, 42)) { c =>
        //for (i <- 0 until 16) {
        //    for (j <- 0 until 16) {
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" CC 0  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                
                c.io.push.poke(1.B)
                c.io.data_in.poke(1398101.U)
        
                println(s" Poked push and data ")
        
                println(s" ***************************** ")        
                c.clock.step(1) // CC 1
                println(s" CC 1  ")
                
                println(s" Data written to sync_fifo but not to buffer")    
                //c.io.push.poke(1.B)
                //c.io.data_in.poke(1398101.U)
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)
        
                println(s" ***************************** ")        
                c.clock.step(1)  // CC 2
                println(s" CC 2  ")
        
                println(s" Data written to buffer") 
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                println(s" Pull not asserted yet")
                println(s" data_out:${c.io.data_out.peek()}")
        
                c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
        
                println(s" ***************************** ")
                c.clock.step(1) // CC 3
                println(s" CC 3  ")
                c.io.push.poke(0.B)
                println(s" Removed push ")        
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                c.io.pull.poke(1.B)
                println(s" Pull asserted ")
                println(s" data_out:${c.io.data_out.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1) // CC 4
                println(s" CC 4  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                c.io.full.expect(0.B)
  
                println(s" ***************************** ")
                c.clock.step(1)  // CC 5
                println(s" CC 5  ")
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")

                c.io.full.expect(0.B)
  
                println(s" ***************************** ")
                c.clock.step(1)  // CC 6
                println(s" CC 6  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 7
                println(s" CC 7  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 8
                println(s" CC 8  ")
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 9
                println(s" CC 9  ")
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        
                println(s" ***************************** ")
                c.clock.step(1)  // CC 10
                println(s" CC 10  ")
                
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" data_out:${c.io.data_out.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                
				c.io.empty.expect(1.B)
                println(s" *Test still has pull asserted, empty should be asserted")
                println(s" ***************************** ")
       
                
        //    }
       // }
    }
    println(getVerilog(new funnel_shifter(4, 21, 9, 42)))
    true
}

assert(test_funnel_shifter)
