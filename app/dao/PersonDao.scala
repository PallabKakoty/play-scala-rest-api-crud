package dao

import javax.inject.{Inject, Singleton}
import models.{Person, Persons}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class PersonDao @Inject() (persons: Persons) {
  object Person {
    //private implicit val logAddress = "dao.personDao.person"
    def savePersonDetail(person: Person): Int = {
      val personFuture = persons.save(person)
      Await.result(personFuture, 3 seconds)
    }

    def findPersonByEmail(email: String): Option[Person] = {
      val personFuture = persons.getPerson(email)
      Await.result(personFuture, 3 second)
    }
  }
}
