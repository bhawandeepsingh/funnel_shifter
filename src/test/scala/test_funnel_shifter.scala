import chisel3._
import chisel3.tester._
import org.scalatest._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._
import treadle._
//import chisel3.iotesters._

//def test_funnel_shifter: Boolean = {
 //   test(new funnel_shifter(4, 21, 9, 42)) { c =>
   
class TesterSimple extends FreeSpec with ChiselScalatestTester {
    //PeekPokeTester (c) {

/*	def test_funnel_shifter_fifo_empty : Boolean = {
		test(new funnel_shifter (4, 37, 11, 74)) { c => 
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
	  			println(s" Empty must still be asserted")  

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
                println(s" Empty must get de-asserted")  
 
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
                      
        }
       true
    }

	    "Funnel shifter initially and for 1CC after a write, empty must work ''" in {
        test_funnel_shifter_fifo_empty
    }
*/


/************************************************************************************************************/

/*
def test_funnel_shifter_full : Boolean = {
		test(new funnel_shifter (3, 37, 11, 74)) { c =>

				c.io.mem_wp_port.expect(0.U)
                c.io.mem_rp_port.expect(0.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)
 
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" CC 0  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
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
	  			println(s" Empty must still be asserted")  

				c.io.mem_wp_port.expect(1.U)
                c.io.mem_rp_port.expect(0.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
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
                println(s" Empty must get de-asserted") 

				c.io.mem_wp_port.expect(2.U)
                c.io.mem_rp_port.expect(1.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(0.U)
 
 
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")


                println(s" ***************************** ")        
                c.clock.step(1)  // CC 3
                println(s" CC 3  ")

				c.io.mem_wp_port.expect(4.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")
				println(s" Buffer should be full i.e. free entries = 0 ")

                println(s" ***************************** ")        
                c.clock.step(1)  // CC 4
                println(s" CC 4  ")

				c.io.mem_wp_port.expect(5.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

 
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")

                println(s" ***************************** ")        
                c.clock.step(1)  // CC 5
                println(s" CC 5  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B)        
        }
       true
    }

	    "Funnelshifter full must work ''" in {
        test_funnel_shifter_full
    }

*/




/**********************************************************************************************************************************************/

def test_funnel_shifter_1 : Boolean = {
		test(new funnel_shifter (3, 37, 11, 74)) { c =>

				c.io.mem_wp_port.expect(0.U)
                c.io.mem_rp_port.expect(0.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)
 
                c.io.full.expect(0.B)
                c.io.empty.expect(1.B)

                println(s" CC 0  ")
        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
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
	  			println(s" Empty must still be asserted")  

				c.io.mem_wp_port.expect(1.U)
                c.io.mem_rp_port.expect(0.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

        
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
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
                println(s" Empty must get de-asserted") 

				c.io.mem_wp_port.expect(2.U)
                c.io.mem_rp_port.expect(1.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(0.U)
 
 
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")


                println(s" ***************************** ")        
                c.clock.step(1)  // CC 3
                println(s" CC 3  ")

				c.io.mem_wp_port.expect(4.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")
				println(s" Buffer should be full i.e. free entries = 0 ")

                println(s" ***************************** ")        
                c.clock.step(1)  // CC 4
                println(s" CC 4  ")

				c.io.mem_wp_port.expect(5.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)

 
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)
				println(s" free entries:${c.io.free_entries_port.peek()}")

                println(s" ***************************** ")        
                c.clock.step(1)  // CC 5
                println(s" CC 5  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B) 

				c.io.push.poke(0.B)    
				println(s" Push deasserted, Funnel shifter full , 5 entries total  ")

 				println(s" ***************************** ")        
                c.clock.step(1)  // CC 6
                println(s" CC 6  ")

				println(s" Pull asserted ")
				c.io.pull.poke (1.B)
				
				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(0.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B)

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 7
                println(s" CC 7  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(11.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B)

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 8
                println(s" CC 8  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(22.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B)

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 9
                println(s" CC 9  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(2.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(33.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(1.B)  

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 10
                println(s" CC 10  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(4.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(44.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 11
                println(s" CC 11  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(4.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(55.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 12
                println(s" CC 12  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(4.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(66.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)  
 
 				println(s" ***************************** ")        
                c.clock.step(1)  // CC 13
                println(s" CC 13  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(5.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(3.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)   

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 14
                println(s" CC 14  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(5.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(14.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)  


				println(s" ***************************** ")        
                c.clock.step(1)  // CC 15
                println(s" CC 15  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(5.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(25.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B) 


				println(s" ***************************** ")        
                c.clock.step(1)  // CC 16
                println(s" CC 16  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(5.U)
				c.io.buffer_wp_port.expect(0.U)
                c.io.buffer_rp_port.expect(36.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)  

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 17
                println(s" CC 17  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(47.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)    

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 18
                println(s" CC 18  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(58.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B) 

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 19
                println(s" CC 19  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(69.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)    

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 20
                println(s" CC 20  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(6.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B) 

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 21
                println(s" CC 21  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(17.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(0.B)
				c.io.full.expect(0.B)    

				println(s" ***************************** ")        
                c.clock.step(1)  // CC 22
                println(s" CC 22  ")

				c.io.mem_wp_port.expect(6.U)
                c.io.mem_rp_port.expect(6.U)
				c.io.buffer_wp_port.expect(37.U)
                c.io.buffer_rp_port.expect(28.U)
         
                println(s" mem_wp:${c.io.mem_wp_port.peek()}")
				println(s" mem_rp:${c.io.mem_rp_port.peek()}")
                println(s" buf_wp:${c.io.buffer_wp_port.peek()}")
                println(s" buf_rp:${c.io.buffer_rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
				println(s" free entries:${c.io.free_entries_port.peek()}")

        		c.io.empty.expect(1.B)
				c.io.full.expect(0.B)  
  
        }
       true
    }

	    "Funnelshifter full must work ''" in {
        test_funnel_shifter_1
    }



/********************************************************************************************************************************************/  
/*	def test_funnel_shifter_0 : Boolean = {
		test(new funnel_shifter (4, 21, 9, 42)) { c => 
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
        }
       true
    }

	    "FUnnel shifter must work ''" in {
        test_funnel_shifter_0
    }*/

}

