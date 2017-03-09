package iohk.scorex.bitcoin

import java.io.File

import org.bitcoinj.core.{BlockChain, Context, PeerGroup}
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.store.SPVBlockStore
import org.bitcoinj.wallet.Wallet


trait MainnetAnalyzer {

  val netParams = MainNetParams.get()

  val Folder = "/tmp/"
  new File(Folder).mkdirs()
  lazy val store = new SPVBlockStore(netParams, new File(Folder+"blockchain"))

  lazy val context = new Context(netParams)
  lazy val chain = new BlockChain(context, new Wallet(context), store)

  def chainDownload(): Unit = {

    val peerGroup = new PeerGroup(netParams, chain)
    peerGroup.setUserAgent("kushti's bot", "0.1")
    peerGroup.addPeerDiscovery(new DnsDiscovery(netParams))
    peerGroup.startAsync()
    peerGroup.downloadBlockChain()
  }
}
