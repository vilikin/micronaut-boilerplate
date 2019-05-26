package micronaut.server.core

import io.reactiverse.pgclient.Row
import io.reactiverse.reactivex.pgclient.PgPool
import io.reactiverse.reactivex.pgclient.PgRowSet
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import javax.inject.Singleton

class MissingValueException(missingField: String) : Exception("Couldn't find value for field $missingField")

data class Person(
  val id: Int,
  val name: String,
  val age: Int
) {
  companion object {
    fun createFromRow(row: Row): Person {
      return Person(
        id = row.getInteger("id") ?: throw MissingValueException("id"),
        name = row.getString("name") ?: throw MissingValueException("name"),
        age = row.getInteger("age") ?: throw MissingValueException("age")
      )
    }
  }
}

@Singleton
class PersonService(
  val client: PgPool
) {
  fun findAllPersons(): Observable<Person> {
    return client.rxQuery("SELECT * FROM persons;")
      .toRows()
      .map { Person.createFromRow(it) }
  }
}

fun Single<PgRowSet>.toRows(): Observable<Row> = this.flatMapObservable { it.iterator().delegate.toObservable() }

