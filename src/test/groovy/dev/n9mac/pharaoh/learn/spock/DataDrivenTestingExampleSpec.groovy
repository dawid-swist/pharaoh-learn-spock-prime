package dev.n9mac.pharaoh.learn.spock

import groovy.sql.Sql
import spock.lang.Rollup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.mop.Use

class DataDrivenTestingExampleSpec extends Specification {


    // Usage data tables
    @Rollup
    def "Maximum of two numbers"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 3 | 3
        7 | 4 | 7
        0 | 0 | 0
    }


    def "Sum of a and b"(int a, int b, int c) {
        expect:
        a + b == c

        where:
        c  | _
        6  | _
        11 | _
        __ // alternative

        a | b
        3 | 3
        4 | 7
    }

    // the where: block already declares all data variables, the method parameters can be omitted bu using ||

    def "maximum of two numbers - 3"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b || c
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0
    }

    // or ; ;;
    def "maximum of two numbers - 4"() {
        expect:
        Math.max(a, b) == c

        where:
        a; b; ; c
        1; 3; ; 3
        7; 4; ; 7
        0; 0; ; 0
    }

    // Data pipes
    def "maximum of two numbers - 5"() {
        expect:
        Math.max(a, b) == c

        where:
        a << [1, 7, 0]
        b << [3, 4, 0]
        c << [3, 7, 0]
    }


    // Setup database during setup
    @Shared
            sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

    def setupSpec() {
        sql.execute("CREATE TABLE maxdata (a int, b int, c int);")
        sql.execute("insert into maxdata values(1, 1,1);")
        sql.execute("insert into maxdata values(2, 3,3);")
        sql.execute("insert into maxdata values(7, 8,8);")
    }

    def "maximum of two numbers - 6"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        where:
        [a, b, c] << sql.rows("select a, b, c from maxdata")
    }

    // cleanup database
    def cleanupSpec() {
        sql = null;
    }

    // Data Variable Assignment
    def "maximum of two numbers - 7"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        where:
        a = 3
        b = Math.random() * 100
        c = a > b ? a : b
    }

    // Accessing Other Data Variables
    def "maximum of two numbers - 8"(int a, int b) {
        expect:
        a < b

        where:
        a | b
        3 | a + 1
        7 | a + 2
        0 | a + 3
    }

    // Multi-Variable Assignment
    def "maximum of two numbers multi-assignment of a=#a b=#b c=#c"() {
        expect:
        Math.max(a, b) == c

        where:
        row << sql.rows("select a, b, c from maxdata")
        (a, b, c) = row
    }

//    Type Coercion for Data Variable Values

    def "type coercion for data variable values"(Integer i) {
        expect:
        i instanceof Integer
        i == 10

        where:
        i = "10"
    }

}

@Use(CoerceBazToBar)
class Foo extends Specification {
    def foo(Bar bar) {
        expect:
        bar == Bar.FOO

        where:
        bar = Baz.FOO
    }
}
enum Bar { FOO, BAR }
enum Baz { FOO, BAR }
class CoerceBazToBar {
    static Bar asType(Baz self, Class<Bar> clazz) {
        return Bar.valueOf(self.name())
    }
}