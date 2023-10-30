package dev.n9mac.pharaoh.learn.spock

import spock.lang.Shared
import spock.lang.Specification


class Pc {
    String vendor
    int clockRate
    int ram
    String os

    static Pc getStandardConfiguration() {
        Pc pc = new Pc()

        pc.setOs("Linux")
        pc.setRam(32 * 1024 * 1024)
        pc.setVendor("Sunny")
        pc.setClockRate(8 * 1024)
        return pc
    }
}

class T01FirstExample extends Specification {

    def field = ""
    def stack

    @Shared
    String res = "Shared field"

    def cleanup() {}

    def cleanupSpec() {}

    // Default setup method for spec field
    def setup() {
        field = "field-1"
        stack = new Stack()
        assert stack.empty()

    }

    def setupSpec() {
        res = "Shared value-1"
    }

    def "pushing an element on the stack"() {
        given:
        def stack = new Stack()
        def elem = "push me"

        when:
        stack.push(elem)

        then:
        !stack.empty
        stack.size() == 1
        stack.peek() == elem
    }

    def "should thrown exception then stack is empty"() {
        given:
        stack = new Stack()

        when:
        stack.pop()

        then:
        thrown(EmptyStackException)
        stack.empty()
    }

    def "HashMap accepts null key"() {
        given:
        def map = new HashMap()

        when:
        map.put(null, "elem")

        then:
        notThrown(NullPointerException)
    }


    def "Math.max should find max from values"() {
        expect:
        Math.max(1, 2) == 2
    }


//    "Example how to use where: expression
    def "computing the maximum of two numbers"() {
        expect:
        Math.max(a, b) == c

        where:
        a << [5, 3]
        b << [1, 9]
        c << [5, 9]
    }

    // Helper method
    void matchesPreferredConfiguration(pc) {
        assert pc.vendor == "Sunny"
        assert pc.clockRate >= 2333
        assert pc.ram >= 4096
//        assert pc.os == "Linux2" generate error and report
        assert pc.os == "Linux"
    }

    def "offered PC matches preferred configuration"() {
        when:
        def pc = Pc.getStandardConfiguration()

        then:
        matchesPreferredConfiguration(pc)
    }

    // Alternative verifyAll
    def "offered PC matches preferred configuration - version with veryfiAll"() {
        when:
        def pc = Pc.getStandardConfiguration()

        then:
        verifyAll(pc) { //Alternative with(pc)
            vendor == "Sunny"
            clockRate >= 2333
            ram >= 406
            os == "Linux"
        }
    }


//    Specifications as Documentation example:
//        given: "open a database connection"
//        and: "seed the customer table"
// code goes here

//        and: "seed the product table"
// code goes here

// ...
//        when: "the account is credited \$10"
// ...

//        then: "the account's balance is \$10"
// ...





}