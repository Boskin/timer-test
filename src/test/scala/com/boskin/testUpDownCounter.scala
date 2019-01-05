package com.boskin

import chisel3._
import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester

class TestUpDownCounter(min: Int, max: Int, dut: UpDownCounter)
  extends PeekPokeTester(dut) {
    
  val diff: Int = max - min

  poke(dut.io.en, 1)
  poke(dut.io.dir, UpDownCounter.up)

  step(1)
  
  expect(dut.io.count, min + 1)
  expect(dut.io.rollover, 0)

  step(diff - 1)

  expect(dut.io.count, max)
  expect(dut.io.rollover, 0)

  step(1)

  expect(dut.io.count, min)
  expect(dut.io.rollover, 1)

  poke(dut.io.dir, UpDownCounter.down)

  step(1)

  expect(dut.io.count, max)
  expect(dut.io.rollover, 1)
}

object TestUpDownCounterMain extends App {
  val min: Int = 0
  val max: Int = 255

  chisel3.iotesters.Driver.execute(args,
    () => new UpDownCounter(min, max)) {

    dut => new TestUpDownCounter(min, max, dut)
  }
}
