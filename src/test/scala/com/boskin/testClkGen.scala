package com.boskin

import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester

class TestClkGen(dut: ClkGen) extends PeekPokeTester(dut: ClkGen) {
  step(20)
}

object TestClkGenMain extends App {
  val inFreq: Double = 100e6
  val outFreq: Double = 25e6

  chisel3.iotesters.Driver.execute(args, () => new ClkGen(inFreq, outFreq)) {
    dut => new TestClkGen(dut)
  }
}
