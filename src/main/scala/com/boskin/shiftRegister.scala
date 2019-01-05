package com.boskin

import chisel3._

class ShiftRegister[T <: Data](gen: T, length: Int) extends Module {
  val io = IO(new Bundle {
    val din = Input(gen)
    val en = Input(Bool())
    val dout = Output(gen)
  })

  val shiftReg = Reg(Vec(length, gen))
  when (io.en) {
    shiftReg(0) := io.din
    for (i <- 1 until length) {
      shiftReg(i) := shiftReg(i - 1)
    }
  }
  io.dout := shiftReg(length - 1)
}

object CDC {
  def apply[T <: Data](gen: T, sig: T, length: Int): T = {
    val shiftRegInst = Module(new ShiftRegister(gen, length))
    shiftRegInst.io.din := sig
    shiftRegInst.io.en := true.B
    shiftRegInst.io.dout
  }
  def apply(sig: Bool): Bool = {
    CDC(Bool(), sig, 2)
  }
}
