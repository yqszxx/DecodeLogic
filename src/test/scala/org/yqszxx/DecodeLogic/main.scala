package org.yqszxx.DecodeLogic

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import Constants._

/*
 * Seven segment display driver
 *  Digit  Code        Segments
 *                   A B C D E F G
 *    0    0000      1 1 1 1 1 1 0          -A-
 *    1    0001      0 1 1 0 0 0 0         |   |
 *    2    0010      1 1 0 1 1 0 1         F   B
 *    3    0011      1 1 1 1 0 0 1         |   |
 *    4    0100      0 1 1 0 0 1 1          -G-
 *    5    0101      1 0 1 1 0 1 1         |   |
 *    6    0110      1 0 1 1 1 1 1         E   C
 *    7    0111      1 1 1 0 0 0 0         |   |
 *    8    1000      1 1 1 1 1 1 1          -D-
 *    9    1001      1 1 1 1 0 1 1
 */
class SevenSegDriver extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(4.W))
    val out = Output(Vec(4, Bool()))
  })

  def default: List[BitPat] =
    //   A       E
    //   | B     | F
    //   | | C   | | G
    //   | | | D | | |
    List(X,X,X,X,X,X,X)

  val table: Array[(BitPat, List[BitPat])] = Array(
    BitPat("b0000") -> List(Y,Y,Y,Y,Y,Y,N),  // 0
    BitPat("b0001") -> List(N,Y,Y,N,N,N,N),  // 1
    BitPat("b0010") -> List(Y,Y,N,Y,Y,N,Y),  // 2
    BitPat("b0011") -> List(Y,Y,Y,Y,N,N,Y),  // 3
    BitPat("b0100") -> List(N,Y,Y,N,N,Y,Y),  // 4
    BitPat("b0101") -> List(Y,N,Y,Y,N,Y,Y),  // 5
    BitPat("b0110") -> List(Y,N,Y,Y,Y,Y,Y),  // 6
    BitPat("b0111") -> List(Y,Y,Y,N,N,N,N),  // 7
    BitPat("b1000") -> List(Y,Y,Y,Y,Y,Y,Y),  // 8
    BitPat("b1001") -> List(Y,Y,Y,Y,N,Y,Y),  // 9
//    BitPat("b1010") -> List(X,X,X,X,X,X,X),  // A
//    BitPat("b1011") -> List(X,X,X,X,X,X,X),  // B
//    BitPat("b1100") -> List(X,X,X,X,X,X,X),  // C
//    BitPat("b1101") -> List(X,X,X,X,X,X,X),  // D
//    BitPat("b1110") -> List(X,X,X,X,X,X,X),  // E
//    BitPat("b1111") -> List(X,X,X,X,X,X,X),  // F
  )

  io.out.zip(DecodeLogic(io.in, default, table)).foreach({case (x, y) => x := y})
}

class Tester extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(2.W))
    val out = Output(Bool())
  })

  def default: List[BitPat] = List(X)

  val table: Array[(BitPat, List[BitPat])] = Array(
    BitPat("b10") -> List(N),
    BitPat("b01") -> List(N),
  )

  io.out := DecodeLogic(io.in, default, table).head
}

object main extends App {
//  println(ChiselStage.emitVerilog(new SevenSegDriver))
  println(ChiselStage.emitVerilog(new Tester))
}
