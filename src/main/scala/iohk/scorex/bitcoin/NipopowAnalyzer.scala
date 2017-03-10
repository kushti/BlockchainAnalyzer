package iohk.scorex.bitcoin


import org.bitcoinj.core.{Block, StoredBlock}
import org.bitcoinj.core.listeners.NewBestBlockListener

import scala.collection.mutable

object NipopowAnalyzer extends App with MainnetAnalyzer{

  val m = 5

  val headers = mutable.Map[BigInt, StoredBlock]()

  val bbl = new NewBestBlockListener{
    override def notifyNewBestBlock(block: StoredBlock): Unit = {
      val height = block.getHeight
      val header = block.getHeader

      def mnOpt(k:Int):Option[(Int, Block)]= {
        val m = (1 to k).foldLeft(2){case (mc, _) => mc * 2}
        val blocks = headers.filterKeys(_ < BigInt(header.getDifficultyTargetAsInteger) / m).values

        blocks.isEmpty match {
          case true => None
          case false =>
            val b = blocks.maxBy(_.getHeight)
            Some(b.getHeight -> b.getHeader)
        }
      }

      println(height + " : " + header.getHashAsString)
      println("target: " + header.getDifficultyTargetAsInteger)
      (1 to 20).foreach(k=>
        mnOpt(k).foreach{case (h, bh) =>
          println(s"m$k : height = $h, hash = ${bh.getHash}")
        }
      )

      headers.put(block.getHeader.getHash.toBigInteger, block)
    }
  }


  chain.addNewBestBlockListener(bbl)

  chainDownload()
}
