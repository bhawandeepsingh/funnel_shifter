# funnel_shifter
funnel_shifter in chisel

Chisel code for funnel shifter using a synchronous fifo working tested in jupyter notebook
The working jupyter notebook cells for design and testing of funnel_shifter and sync_fifo and  pushed under model directory

There is no scala model sine the module is more about timing and not very datapath intensive.
So, a scala model may not help much as a proof of concept of the project but will take extra time away from the actual chisel module, will add later if/ when verification requires it.

Chisel code for funnel_shifter is fully complete.
Write and read side widths are parameterizable, any of the two can be bigger by any amount, wider need not be a proper multiple of the narrower, memory size and buffer size are parameterizable, only constraints (that come from system) are that buffer_size should be more than the read side width so a single output word can be output is a single CC, and buffer_size is kept a multiple of writing side width/ memory_word_size  to be able to store0 entire word read from memory in the buffer.

pointers have been left as top level ports to ease testing, but are easy to comment out

jupyter notebook code in model directory, once sbt is working will be ported to sbt/src/main`

sbt WIP. 

Till a commit message says "sbt tested", please just ignore the gcd reference files and just copy the -> "funnel_shifter/src/main/scala/gcd/funnel_shifter.scala" into a jupyter notebook to test circuit functionality. 

sbt WIP.
