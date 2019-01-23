package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

case class Person(id: Option[Int], name: String, email: String, country: String)

@Singleton
class Persons @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  private class PersonTableDef(tag: Tag) extends Table[Person](tag, "person") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def country = column[String]("country")
    def * = (id.?, name, email, country) <> (Person.tupled, Person.unapply)
  }

  private def personData = TableQuery[PersonTableDef]
  private def autoInc = personData returning personData.map(_.id)

  def save(person: Person): Future[Int] = {
    db.run(autoInc += person)
  }

  def update(person: Person): Future[Int] = {
    db.run(personData.filter(_.id === person.id).map(x=> (x.name, x.country)).update((person.name, person.country)))
  }

  def getPerson(email: String): Future[Option[Person]] = {
    db.run(personData.filter(_.email === email).result.headOption)
  }

  def delete(email: String): Future[Int] = {
    db.run(personData.filter(_.email === email).delete)
  }
}
