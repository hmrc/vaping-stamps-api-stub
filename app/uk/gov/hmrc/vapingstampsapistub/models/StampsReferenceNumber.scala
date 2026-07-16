package uk.gov.hmrc.vapingstampsapistub.models

case class StampsReferenceNumber(srn: String) {
  override def toString: String = srn
  
  private def prefix: String = srn.take(4)

  def isNorthernIreland: Boolean = prefix startsWith "XI"
  
  def digits: String = srn.filter(_.isDigit)
}

object StampsReferenceNumber {
  val regex = ""
  
  //Checks for valid String
  def verify = ???
  
  //read if string is valid this is where we implement our verify
  given reads = ???
}
