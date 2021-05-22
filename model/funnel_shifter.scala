
class funnel_shifter (mem_size : Int, in_word_size : Int, out_word_size : Int) extends Module
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
    
    val buffer_size = in_word_size*4  // Can parameterize 4 if needed
    val buffer = RegInit(0.U(buffer_size.W)) 
    val free_entries = RegInit (buffer_size.U(log2Ceil(buffer_size).W))
    val buffer_wp = RegInit (0.U(log2Ceil(buffer_size).W))
    val buffer_rp = RegInit (0.U(log2Ceil(buffer_size).W))
    
    //temp
    io.buffer_wp_port := buffer_wp
    io.buffer_rp_port := buffer_rp
    //temp over
    
    when (free_entries < out_word_size.U)
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
        buffer_rp := buffer_rp + out_word_size.U // Assuming truncation - Check 3
    }
    
    // wp and buffer_update - handle wraparound
    when (fifo_inst.io.pull)
    {
        buffer_wp := buffer_wp + in_word_size.U // Assuming truncation - Check 4
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
    
}






