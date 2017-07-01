package controllers

import javax.inject._

import com.redis.RedisClient
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc._

import scala.util.Random

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val shareForm = Form(
    mapping(
      "text" -> nonEmptyText
    ) (SharedData.apply)(SharedData.unapply)
  )


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Ok(views.html.index(shareForm))
  }

  def share = Action {
    implicit request =>
      val errorFunction = { formWithErrors: Form[SharedData] =>
        Redirect(routes.HomeController.index()).flashing("info" -> "Sharing text failed! Try again!")
      }

      val successFunction = { data: SharedData =>
        val key = Random.alphanumeric.take(10).mkString

        val r = new RedisClient("localhost", 6379)
        r.hmset("shared", Map(key -> data.text))
        r.disconnect

        Redirect(routes.HomeController.view(key))
      }

      val formValidationResult = shareForm.bindFromRequest
      formValidationResult.fold(errorFunction, successFunction)
  }

  def view(key: String) = Action {
    implicit request =>
      val r = new RedisClient("localhost", 6379)
      val shared = r.hmget("shared", key).get
      r.disconnect

      Ok(views.html.view(shared(key), key))
  }
}

case class SharedData(text: String)