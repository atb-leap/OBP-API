package code.atms

/* For atms */

// Need to import these one by one because in same package!

import code.atms.Atms.{AtmT, AtmId}
import code.model.BankId
import code.common._
import net.liftweb.common.Logger
import net.liftweb.util.SimpleInjector

object Atms extends SimpleInjector {

  case class AtmId(value : String)

  object AtmId {
    def unapply(id : String) = Some(AtmId(id))
  }

  trait AtmT {
    def atmId : AtmId
    def bankId : BankId
    def name : String
    def address : AddressT
    def location : LocationT
    def meta : MetaT

    def  OpeningTimeOnMonday : Option[String]
    def  ClosingTimeOnMonday : Option[String]

    def  OpeningTimeOnTuesday : Option[String]
    def  ClosingTimeOnTuesday : Option[String]

    def  OpeningTimeOnWednesday : Option[String]
    def  ClosingTimeOnWednesday : Option[String]

    def  OpeningTimeOnThursday : Option[String]
    def  ClosingTimeOnThursday: Option[String]

    def  OpeningTimeOnFriday : Option[String]
    def  ClosingTimeOnFriday : Option[String]

    def  OpeningTimeOnSaturday : Option[String]
    def  ClosingTimeOnSaturday : Option[String]

    def  OpeningTimeOnSunday: Option[String]
    def  ClosingTimeOnSunday : Option[String]

    def  isAccessible : Option[Boolean]

    def  branchType : Option[String]
    def  moreInfo : Option[String]


  }

  case class Atm (
    atmId : AtmId,
    bankId : BankId,
    name : String,
    address : Address,
    location : Location,
    meta : Meta,

    OpeningTimeOnMonday : Option[String],
    ClosingTimeOnMonday : Option[String],

    OpeningTimeOnTuesday : Option[String],
    ClosingTimeOnTuesday : Option[String],

    OpeningTimeOnWednesday : Option[String],
    ClosingTimeOnWednesday : Option[String],

    OpeningTimeOnThursday : Option[String],
    ClosingTimeOnThursday: Option[String],

    OpeningTimeOnFriday : Option[String],
    ClosingTimeOnFriday : Option[String],

    OpeningTimeOnSaturday : Option[String],
    ClosingTimeOnSaturday : Option[String],

    OpeningTimeOnSunday: Option[String],
    ClosingTimeOnSunday : Option[String],

    isAccessible : Option[Boolean],

    branchType : Option[String],
    moreInfo : Option[String]
  )

  val atmsProvider = new Inject(buildOne _) {}

  def buildOne: AtmsProvider = MappedAtmsProvider

  // Helper to get the count out of an option
  def countOfAtms (listOpt: Option[List[AtmT]]) : Int = {
    val count = listOpt match {
      case Some(list) => list.size
      case None => 0
    }
    count
  }


}

trait AtmsProvider {

  private val logger = Logger(classOf[AtmsProvider])


  /*
  Common logic for returning atms.
   */
  final def getAtms(bankId : BankId) : Option[List[AtmT]] = {
    // If we get atms filter them
    getAtmsFromProvider(bankId) match {
      case Some(atms) => {

        val atmsWithLicense = for {
         branch <- atms if branch.meta.license.name.size > 3 && branch.meta.license.name.size > 3
        } yield branch
        Option(atmsWithLicense)
      }
      case None => None
    }
  }

  /*
  Return one Atm
   */
  final def getAtm(branchId : AtmId) : Option[AtmT] = {
    // Filter out if no license data
    getAtmFromProvider(branchId).filter(x => x.meta.license.id != "" && x.meta.license.name != "")
  }

  protected def getAtmFromProvider(branchId : AtmId) : Option[AtmT]
  protected def getAtmsFromProvider(bank : BankId) : Option[List[AtmT]]

// End of Trait
}






