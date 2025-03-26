package com.example.demo.test;
import com.example.demo.model.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListForEach {
  public static void main(String[] args) {
    // Personオブジェクトを格納するListを作成
    List<Person> people = new ArrayList<>();

    // Personオブジェクトを作成してListに追加
    people.add(new Person(1,"Alice", 30));
    people.add(new Person(2,"Bob", 25));
    people.add(new Person(3,"Charlie", 35));

    // Listの内容を表示
    for (Person person : people) {
      System.out.println("before:"+person);
    }

    people.forEach(person -> {
      person.setAge(person.getAge()+1);
    });

    // Listの内容を表示
    for (Person person : people) {
      System.out.println("after age set:"+person);
    }

    people.sort(
        Comparator.comparingInt(Person::getAge)
            .reversed());

    // Listの内容を表示
    for (Person person : people) {
      System.out.println("after age sort:"+person);
    }
  }
}