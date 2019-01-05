package com.boskin

import chisel3._
import chisel3.util.Enum
import chisel3.util.log2Ceil

class UpDownCounter(min: Int, max: Int) extends Module {
  val width: Int = log2Ceil(max - min + 1)
  val io = IO(new Bundle {
    val en = Input(Bool())
    val dir = Input(Bool())
    val count = Output(UInt(width.W))
    val rollover = Output(Bool())
  })

  val countReg = RegInit(min.U(width.W))
  val rolloverReg = RegInit(false.B)
  when (io.en) {
    when (io.dir === UpDownCounter.up) {
      when (countReg === max.U) {
        countReg := min.U
        rolloverReg := true.B
      } .otherwise {
        countReg := countReg + 1.U
        rolloverReg := false.B
      }
    } .otherwise {
      when (countReg === min.U) {
        countReg := max.U
        rolloverReg := true.B
      } .otherwise {
        countReg := countReg - 1.U
        rolloverReg := false.B
      }
    }
  } .otherwise {
    rolloverReg := false.B
  }
  
  io.count := countReg
  io.rollover := rolloverReg
}

object UpDownCounter {
  val up :: down :: Nil = Enum(2)
}

object GenUpDownCounter extends App {
  chisel3.Driver.execute(args, () => new UpDownCounter(0, 255))
}
