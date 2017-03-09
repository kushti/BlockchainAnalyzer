package iohk.scorex.bitcoin

import org.bitcoinj.core.{Block, StoredBlock}
import org.bitcoinj.core.listeners.NewBestBlockListener
import scala.collection.mutable

object NipopowAnalyzer extends App with MainnetAnalyzer{

  val m = 5

  val headers = mutable.Map[Int, Block]()

  val bbl = new NewBestBlockListener{
    override def notifyNewBestBlock(block: StoredBlock): Unit = {
      val height = block.getHeight
      val header = block.getHeader
      println(height + " : " + header.getHashAsString)

      def mnOpt(k:Int):Option[Block]= {
        val m = (1 to k).foldLeft(2){case (mc, _) => mc * 2}
        headers.values
          .find(h => BigInt(h.getHash.toBigInteger) < BigInt(header.getHash.toBigInteger) / m)
      }

      (1 to 16).foreach(k=>
        mnOpt(k).foreach(h => println(s"m$k : " + h.getHash))
      )

      headers.put(height, block.getHeader)
    }
  }


  chain.addNewBestBlockListener(bbl)

  chainDownload()
}
