package com.boskin

import chisel3._
import chisel3.core.{withClock, withClockAndReset}

class Top(clkFreq: Double, counterFreq: Double, counterMin: Int,
  counterMax: Int) extends Module {

  val io = IO(new Bundle {
    val userReset = Input(Bool())
    val en = Input(Bool())
    val disp = Output(UInt(7.W))
  })

  val clkGenInst = Module(new ClkGen(clkFreq, counterFreq))
  val counterClk = clkGenInst.io.clkOut

  val counterReset = withClock(counterClk) {
    CDC(io.userReset)
  }

  withClockAndReset(counterClk, counterReset) {
    val counterInst = Module(new UpDownCounter(counterMin, counterMax))
    counterInst.io.en := CDC(io.en)
    counterInst.io.dir := UpDownCounter.up
    
    val sevenSegDecoderInst = Module(new SevenSegDecoder(true))
    sevenSegDecoderInst.io.din := counterInst.io.count
    io.disp := sevenSegDecoderInst.io.dout
  }
}

object GenTop extends App {
  val clkFreq: Double = 50e6
  val counterFreq: Double = 1.0

  val counterMin: Int = 0
  val counterMax: Int = 15

  chisel3.Driver.execute(args,
    () => new Top(clkFreq, counterFreq, counterMin, counterMax))
}
