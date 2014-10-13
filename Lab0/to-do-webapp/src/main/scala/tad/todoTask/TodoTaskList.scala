package tad.todoTask


case class TodoTaskList (var taskList : List[TodoTask]) {

  def this() = this(List[TodoTask]())

  /**
   * Prepends a new Task to the list
   * cost: O(1)
   */
  def addTask (person: TodoTask) = taskList = taskList.+:(person)

  /**
   * Appends a new Task to the list
   * cost: O(n)
   */
  def addTaskAppend (person: TodoTask) = taskList = taskList.:+(person)
}