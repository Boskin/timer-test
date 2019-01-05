package com.boskin

import chisel3._

class SevenSegDecoder(activeLow: Boolean) extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(4.W))
    val dout = Output(UInt(7.W))
  })

  val decOut = Wire(UInt(7.W))

  switch (io.din) {
    is (0.U) {
      decOut := 0.U
    }
    is (1.U) {
      decOut := "h02".U
    }
    is (2.U) {
      decOut := "h5b".U
    }
    is (3.U) {
      decOut := "h4f".U
    }
    is (4.U) {
      decOut := "h66".U
    }
    is (5.U) {
      decOut := "h6d".U
    }
    is (6.U) {
      decOut := "h7d".U
    }
    is (7.U) {
      decOut := "h07".U
    }
    is (8.U) {
    }
  }
}
