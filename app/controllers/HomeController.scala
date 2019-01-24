package controllers

import dao.PersonDao
import javax.inject.Inject
import models.Person
import play.api.libs.json.Json
import play.api.mvc._

/**
  * A very small controller that renders a home page.
  */
class HomeController @Inject()(cc: ControllerComponents, personDao: PersonDao) extends AbstractController(cc) {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def createProfile() = Action { implicit request =>
    request.body.asJson.map { json =>
      (json \ "name").asOpt[String].map { name =>
        (json \ "email").asOpt[String].map { email =>
          (json \ "country").asOpt[String].map { country =>
            val person = personDao.Person.findPersonByEmail(email)
            if (person.isEmpty) {
              personDao.Person.savePersonDetail(Person(None, name, email, country))
              Ok(Json.obj("res"->"Details Saved"))
            } else (NotAcceptable(Json.obj("err"->"Email id already registered")))
          }.getOrElse(NotFound(Json.obj("err"->"Country not found")))
        }.getOrElse(NotFound(Json.obj("err"->"Email not found")))
      }.getOrElse(NotFound(Json.obj("err"->"Name not found")))
    }.getOrElse(NotFound(Json.obj("err"->"Json data not found")))
  }

  def getPerson() = Action { implicit request =>
    request.body.asJson.map { json =>
      (json \ "email").asOpt[String].map { email =>
        personDao.Person.findPersonByEmail(email).map { person =>
          Ok(Json.obj("name"->person.name, "country"->person.country))
        }.getOrElse(NotFound(Json.obj("err"->"Person not found")))
      }.getOrElse(NotFound(Json.obj("err"->"Email not found")))
    }.getOrElse(NotFound(Json.obj("err"->"Json data not found")))
  }

  def updateNameCountry() = Action { implicit request =>
    request.body.asJson.map { json =>
      (json \ "email").asOpt[String].map { email =>
        (json \ "newName").asOpt[String].map { name =>
          (json \ "newCountry").asOpt[String].map { country =>
            personDao.Person.findPersonByEmail(email).map { person =>
              personDao.Person.updateNameCountry(Person(person.id, name, email, country))
              Ok(Json.obj("res"->"User data updated"))
            }.getOrElse(NotFound(Json.obj("err"->"Person not found")))
          }.getOrElse(NotFound(Json.obj("err"->"Country not found")))
        }.getOrElse(NotFound(Json.obj("err"->"Name not found")))
      }.getOrElse(NotFound(Json.obj("err"->"Email not found")))
    }.getOrElse(NotFound(Json.obj("err"->"Json data not found")))
  }

  def deletePerson() = Action { implicit request =>
    request.body.asJson.map { json =>
      (json \ "email").asOpt[String].map { email =>
        personDao.Person.findPersonByEmail(email).map { person =>
          personDao.Person.deletePerson(email)
          Ok(Json.obj("res"->"Person data deleted"))
        }.getOrElse(NotFound(Json.obj("err"->"User not found")))
      }.getOrElse(NotFound(Json.obj("err"->"Email not found")))
    }.getOrElse(NotFound(Json.obj("err"->"Json data not found")))
  }

}
