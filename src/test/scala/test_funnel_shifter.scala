// See README.md for license details.


import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly gcd.GcdDecoupledTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly gcd.GcdDecoupledTester'
  * }}}
  */

/*class funnel_shifter_Spec extends FreeSpec with ChiselScalatestTester {

  "Gcd should calculate proper greatest common denominator" in {
    test(new DecoupledGcd(16)) { dut =>
      dut.input.initSource()
      dut.input.setSourceClock(dut.clock)
      dut.output.initSink()
      dut.output.setSinkClock(dut.clock)

      val testValues = for { x <- 0 to 10; y <- 0 to 10} yield (x, y)
      val inputSeq = testValues.map { case (x, y) => (new GcdInputBundle(16)).Lit(_.value1 -> x.U, _.value2 -> y.U) }
      val resultSeq = testValues.map { case (x, y) =>
        (new GcdOutputBundle(16)).Lit(_.value1 -> x.U, _.value2 -> y.U, _.gcd -> BigInt(x).gcd(BigInt(y)).U)
      }

      fork {
        // push inputs into the calculator, stall for 11 cycles one third of the way
        val (seq1, seq2) = inputSeq.splitAt(resultSeq.length / 3)
        dut.input.enqueueSeq(seq1)
        dut.clock.step(11)
        dut.input.enqueueSeq(seq2)
      }.fork {
        // retrieve computations from the calculator, stall for 10 cycles one half of the way
        val (seq1, seq2) = resultSeq.splitAt(resultSeq.length / 2)
        dut.output.expectDequeueSeq(seq1)
        dut.clock.step(10)
        dut.output.expectDequeueSeq(seq2)
      }.join()

    }
  }
}
*/



//def test_sync_fifo: Boolean = {
class funnel_shifter_Spec extends FreeSpec with ChiselScalatestTester {
def test_sync_fifo: Boolean = {

//  "Sync fifo should work in" {

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
        
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.full.expect(1.B)
                
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
        
                c.io.data_out.expect(20.U)
                c.clock.step(1)
                println(s" wp:${c.io.wp_port.peek()}")
                println(s" rp:${c.io.rp_port.peek()}")
                println(s" full:${c.io.full.peek()}")
                println(s" empty:${c.io.empty.peek()}")
                println(s" ***************************** ")
        
                c.io.empty.expect(1.B)
        //    }
       // }
    }
    //println(getVerilog(new sync_fifo(4, 32)))
    true
	}
	
	"Sync fifo should work in" {
	test_sync_fifo
	}
}

//assert(test_sync_fifo)
