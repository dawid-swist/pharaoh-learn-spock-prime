package dev.n9mac.pharaoh.learn.spock

import spock.lang.Specification


class T01FirstExample extends Specification {

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
}