# funnel_shifter
funnel_shifter in chisel

Chisel code for sync_fifo working tested in jupyter notebook
The working jupyter notebook cell is pushed under model directory

There is no scala model sine the module is more about timing and not very datapath intensive.
So, a scala model may not help much as a proof of concept of the project but will take extra time away from the actual chisel module, will add later if/ when verification requires it.

Chisel code for funnelshifter is partially complete.
pointers working, reading addresses with and w/o wraparoud working, empty, full working, writing to buffer with and w/o wraparound left"

jupyter notebook code in model directory, once sbt is working will be ported to sbt/src/main`

sbt WIP. 

Till a commit message says "sbt tested", please just ignore the gcd reference files and just copy the -> "funnel_shifter/src/main/scala/gcd/funnel_shifter.scala" into a jupyter notebook to test circuit functionality. 

sbt WIP.
