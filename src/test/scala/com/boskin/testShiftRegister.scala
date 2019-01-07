package com.boskin

import chisel3._
import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester

import scala.util.Random
import scala.collection.mutable.Queue

class TestShiftRegister(dataWidth: Int, length: Int, dut: ShiftRegister[UInt])
  extends PeekPokeTester(dut) {
  
  val randGen = new Random
  val max = 1 << dataWidth
  
  // Non-pipelined tests
  poke(dut.io.en, 1)
  for (i <- 0 until 10) {
    val randNum = randGen.nextInt(max)
    poke(dut.io.din, randNum)
    step(length)
    expect(dut.io.dout, randNum)
  }

  // Pipelined tests
  // Test queue to hold data
  val tests: Queue[Int] = new Queue[Int]
  // Preload the shift register and queue with some data
  for (i <- 0 until length - 1) {
    val randNum = randGen.nextInt(max)

    tests.enqueue(randNum)
    poke(dut.io.din, randNum)

    step(1)
  }

  // Keep loading data onto the queue and pop data off the queue to verify it
  for (i <- 0 until 10) {
    val randNum = randGen.nextInt(max)

    tests.enqueue(randNum)
    poke(dut.io.din, randNum)

    step(1)

    expect(dut.io.dout, tests.dequeue())
  }
}

object TestShiftRegisterMain extends App {
  val dataWidth = 8
  val length = 4
  val gen = UInt(dataWidth.W)

  chisel3.iotesters.Driver.execute(args,
    () => new ShiftRegister(gen, length)) {

    dut => new TestShiftRegister(dataWidth, length, dut)
  }
}
