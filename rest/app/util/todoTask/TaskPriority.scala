package util.todoTask

import play.api.libs.json.Reads
import util.enum.EnumUtils

object TaskPriority extends Enumeration {
  type TaskPriority = Value
  val LOW, MEDIUM, HIGH = Value

  /**
   * Gets the correspondent value to it's name
   * @param name the string to treat
   * @return the matched value, otherwise returns null
   */
  def getValueFromName(name: String) = {
    if(name == null) null else{
        name.toUpperCase match {
        case "LOW" => LOW
        case "MEDIUM" => MEDIUM
        case "HIGH" => HIGH
        case _ => null
      }
    }
  }

  implicit val priorityReads = EnumUtils.enumReads(TaskPriority)
  implicit def priorityWrites = EnumUtils.enumWrites
}