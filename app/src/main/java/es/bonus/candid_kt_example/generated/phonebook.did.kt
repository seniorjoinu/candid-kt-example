package es.bonus.candid_kt_example.generated

import senior.joinu.candid.utils.CodeBlock
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Base64
import kotlin.ByteArray
import kotlin.Int
import kotlin.String
import senior.joinu.candid.serialize.FuncValueSer
import senior.joinu.candid.serialize.NatValueSer
import senior.joinu.candid.serialize.ServiceValueSer
import senior.joinu.candid.serialize.TextValueSer
import senior.joinu.candid.serialize.TypeDeser
import senior.joinu.candid.serialize.ValueSer
import senior.joinu.candid.transpile.SimpleIDLFunc
import senior.joinu.candid.transpile.SimpleIDLPrincipal
import senior.joinu.candid.transpile.SimpleIDLService
import senior.joinu.candid.utils.Code
import senior.joinu.candid.utils.EdDSAKeyPair

typealias Phone = BigInteger

val PhoneValueSer: ValueSer<BigInteger> = NatValueSer

typealias Name = String

val NameValueSer: ValueSer<String> = TextValueSer

data class Entry(
  val name: Name,
  val description: String,
  val phone: Phone
)

object EntryValueSer : ValueSer<Entry> {
  val nameValueSer: ValueSer<Name> = NameValueSer

  val descriptionValueSer: ValueSer<String> = TextValueSer

  val phoneValueSer: ValueSer<Phone> = PhoneValueSer

  override fun calcSizeBytes(value: Entry): Int = this.nameValueSer.calcSizeBytes(value.name) +
      this.descriptionValueSer.calcSizeBytes(value.description) +
      this.phoneValueSer.calcSizeBytes(value.phone)

  override fun ser(buf: ByteBuffer, value: Entry) {
    this.nameValueSer.ser(buf, value.name)
    this.descriptionValueSer.ser(buf, value.description)
    this.phoneValueSer.ser(buf, value.phone)
  }

  override fun deser(buf: ByteBuffer): Entry = Entry(this.nameValueSer.deser(buf),
      this.descriptionValueSer.deser(buf), this.phoneValueSer.deser(buf))

  override fun poetize(): String = Code.of("%T", EntryValueSer::class)
}

typealias MainActorValueSer = ServiceValueSer

typealias AnonFunc0ValueSer = FuncValueSer

class AnonFunc0(
  funcName: String?,
  service: SimpleIDLService?
) : SimpleIDLFunc(funcName, service) {
  suspend operator fun invoke(
    arg0: Name,
    arg1: String,
    arg2: Phone
  ) {
    val arg0ValueSer = NameValueSer
    val arg1ValueSer = senior.joinu.candid.serialize.TextValueSer
    val arg2ValueSer = PhoneValueSer
    val valueSizeBytes = 0 + arg0ValueSer.calcSizeBytes(arg0) + arg1ValueSer.calcSizeBytes(arg1) +
        arg2ValueSer.calcSizeBytes(arg2)
    val sendBuf = ByteBuffer.allocate(staticPayload.size + valueSizeBytes)
    sendBuf.order(ByteOrder.LITTLE_ENDIAN)
    sendBuf.put(staticPayload)
    arg0ValueSer.ser(sendBuf, arg0)
    arg1ValueSer.ser(sendBuf, arg1)
    arg2ValueSer.ser(sendBuf, arg2)
    val sendBytes = sendBuf.array()

    val receiveBytes = this.service!!.call(this.funcName!!, sendBytes)
    val receiveBuf = ByteBuffer.wrap(receiveBytes)
    receiveBuf.order(ByteOrder.LITTLE_ENDIAN)
    receiveBuf.rewind()
    val deserContext = TypeDeser.deserUntilM(receiveBuf)
  }

  companion object {
    val staticPayload: ByteArray = Base64.getDecoder().decode("RElETAJxfQMAcQE=")
  }
}

typealias AnonFunc1ValueSer = FuncValueSer

class AnonFunc1(
  funcName: String?,
  service: SimpleIDLService?
) : SimpleIDLFunc(funcName, service) {
  suspend operator fun invoke(arg0: Name): Entry? {
    val arg0ValueSer = NameValueSer
    val valueSizeBytes = 0 + arg0ValueSer.calcSizeBytes(arg0)
    val sendBuf = ByteBuffer.allocate(staticPayload.size + valueSizeBytes)
    sendBuf.order(ByteOrder.LITTLE_ENDIAN)
    sendBuf.put(staticPayload)
    arg0ValueSer.ser(sendBuf, arg0)
    val sendBytes = sendBuf.array()

    val receiveBytes = this.service!!.query(this.funcName!!, sendBytes)
    val receiveBuf = ByteBuffer.wrap(receiveBytes)
    receiveBuf.order(ByteOrder.LITTLE_ENDIAN)
    receiveBuf.rewind()
    val deserContext = TypeDeser.deserUntilM(receiveBuf)
    return senior.joinu.candid.serialize.OptValueSer( EntryValueSer ).deser(receiveBuf) as Entry?
  }

  companion object {
    val staticPayload: ByteArray = Base64.getDecoder().decode("RElETAFxAQA=")
  }
}

class MainActor(
  host: String,
  canisterId: SimpleIDLPrincipal?,
  keyPair: EdDSAKeyPair?,
  apiVersion: String = "v1"
) : SimpleIDLService(host, canisterId, keyPair, apiVersion) {
  val insert: AnonFunc0 = AnonFunc0("insert", this)

  val lookup: AnonFunc1 = AnonFunc1("lookup", this)
}
