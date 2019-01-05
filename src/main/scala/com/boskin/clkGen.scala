package com.boskin

import chisel3._

import scala.math.round

class ClkGen(inFreq: Double, outFreq: Double) extends Module {
  val countThresh: Int = round(inFreq / (2.0 * outFreq)).toInt

  val io = IO(new Bundle {
    val clkOut = Output(Clock())
  })

  if (countThresh > 1) {
    val counterInst = Module(new UpDownCounter(0, countThresh - 1))
    counterInst.io.en := true.B
    counterInst.io.dir := UpDownCounter.up
    io.clkOut := counterInst.io.rollover.asClock
  } else {
    val clkReg = RegInit(false.B)
    clkReg := ~clkReg
    io.clkOut := clkReg.asClock
  }
}

object GenClkGen extends App {
  chisel3.Driver.execute(args, () => new ClkGen(50.0, 25.0))
}
