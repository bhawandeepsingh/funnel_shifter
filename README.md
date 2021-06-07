# funnel_shifter
funnel_shifter in chisel

Chisel code for funnel shifter using a synchronous fifo.
Working tested in jupyter notebook and sbt

A funnel shifter is an RTL module with input data of width "in_word_size" , output data of width "out_word_size", where in_word_size and out_word_size may or may not have any relationship.

Real systems can also involve clock crossing, not addressed here, does not impact the funnel shifter part, since funnel shifter becomes active after clock crossing is already finished.

The working files for design and testing of funnel_shifter and sync_fifo are  pushed under src/main/scala and src/test/scala, respectively.

The working jupyter notebook cells for design and testing of funnel_shifter and sync_fifo are  pushed under model directory

Chisel code for funnel_shifter is fully complete.
Write and read side widths are parameterizable, any of the two can be bigger by any amount, wider need not be a proper multiple of the narrower, memory size and buffer size are parameterizable, only constraints (that come from system) are that buffer_size should be more than the read side width so a single output word can be output is a single CC, and buffer_size is kept a multiple of writing side width/ memory_word_size  to be able to store0 entire word read from memory in the buffer.

Pointers have been left as top level ports to ease testing with peek.

The slides in the repo contain the design diagram and details about what is parameterizable and what is tested.

To run, from inside the funnel_shifter directory (where build.sbt is present), type -  
```sbt test```

This will run one test of the five tests in he test_funnel_shifter.scala file. 
Any of the other tests can be run by commenting out the scala function for the active test and removing the comments around the test desired to be run. 

Limitations - Future possible enhancements - 
1. CDC can be added  
2. Either of in_word_size or out_word_size or even both can be variable rather than constant. They can be made ports rather than parameters and instantaneous value can be used for each write and read. Handling full and empty instantaneously for a cycle based on instantaneous in_word_size and out_word_size, respectively, will be interesting. By logical extension, any size that is different from memory width, it’s interface will need a buffer in the form of vector of bits.  
3. The present design only sends one word per clock cycle from FIFO to Buffer even if FIFO Has more words available and Buffer can take more words. This causes more clock cycles if in_word_size << out_word_size even though data may be transferred in 1CC. The width of this transfer may be increased with num_requested being communicated from Funnel_shifter_manger to FIFO and num_returned communicated from FIFO to Funnel shifter manager and BUffer update.  
4. If the FIFO is empty, and buffer has partial data or not, when new data comes, it still passes through the FIFO, this may be byoassed in cases where there is no CDC and data may beeither directly stored in Buffer or if enough data is available, then sent out directly without losing clock cycles.  

