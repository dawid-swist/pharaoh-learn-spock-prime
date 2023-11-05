package dev.n9mac.pharaoh.learn.spock

import spock.lang.Specification


class Publisher {
    List<Subscriber> subscribers = []
    int messageCount = 0

    void send(String message) {
        subscribers*.receive(message)
        messageCount++
    }
}

interface Subscriber {
    void receive(String message)
}

class PublisherSpec extends Specification {
    Publisher publisher = new Publisher()
}

class InteractionBasedTestingSpec extends Specification {

    Publisher publisher = new Publisher()
    Subscriber subscriber = Mock()
    Subscriber subscriber2 = Mock()

    def setup() {
        publisher.subscribers << subscriber // << is a Groovy shorthand for List.add()
        publisher.subscribers << subscriber2
    }

    def "should send messages to all subscribers"() {
        when:
        publisher.send("hello")

        then:
        1 * subscriber.receive("hello")
        1 * subscriber2.receive("hello")
    }

    // Cardinality
    // 1 * subscriber.receive("hello")      // exactly one call
    //0 * subscriber.receive("hello")      // zero calls
    //(1..3) * subscriber.receive("hello") // between one and three calls (inclusive)
    //(1.._) * subscriber.receive("hello") // at least one call
    //(_..3) * subscriber.receive("hello") // at most three calls
    //_ * subscriber.receive("hello")      // any number of calls, including zero
    //                                     // (rarely needed; see 'Strict Mocking')

    //1 * subscriber.receive("hello") // a call to 'subscriber'
    //1 * _.receive("hello")          // a call to any mock object

    // 1 * subscriber.receive("hello") // a method named 'receive'
    // 1 * subscriber./r.*e/("hello")  // a method whose name matches the given regular expression
    // (here: method name starts with 'r' and ends in 'e')

    // 1 * subscriber.receive("hello")        // an argument that is equal to the String "hello"
    //1 * subscriber.receive(!"hello")       // an argument that is unequal to the String "hello"
    //1 * subscriber.receive()               // the empty argument list (would never match in our example)
    //1 * subscriber.receive(_)              // any single argument (including null)
    //1 * subscriber.receive(*_)             // any argument list (including the empty argument list)
    //1 * subscriber.receive(!null)          // any non-null argument
    //1 * subscriber.receive(_ as String)    // any non-null argument that is-a String
    //1 * subscriber.receive(endsWith("lo")) // an argument matching the given Hamcrest matcher
    //                                       // a String argument ending with "lo" in this case
    //1 * subscriber.receive({ it.size() > 3 && it.contains('a') })
    //// an argument that satisfies the given predicate, meaning that
    //// code argument constraints need to return true of false
    //// depending on whether they match or not
    //// (here: message length is greater than 3 and contains the character a)


    // Stubbing


    interface Subscriber2 {
        String receive(String message)
    }

    def "should stub invocation"() {
        when:
        Subscriber2 subscriber2 = Mock()
        subscriber2.receive(_) >> "ok"

        then:
        subscriber2.receive("Some-subscriber") == "ok"
    }

    def "Returning Sequences of Values"() {
        when:
        Subscriber2 subscriber2 = Mock()
        subscriber2.receive(_) >>> ["ok", "error", "error", "ok"]

        then:
        subscriber2.receive("Some-subscriber") == "ok"
        subscriber2.receive("Some-subscriber") == "error"
        subscriber2.receive("Some-subscriber") == "error"
        subscriber2.receive("Some-subscriber") == "ok"
    }

    def "Computing Return Values"() {
        when:
        Subscriber2 subscriber2 = Mock()
        subscriber2.receive(_) >> { String message -> message.size() > 3 ? "ok" : "fail" }

        then:
        subscriber2.receive("AS") == "fail"
        subscriber2.receive("MyA") == "fail"
        subscriber2.receive("MyBCV") == "ok"
    }

    // other example

    // Performing Side Effects
    // subscriber.receive(_) >> { throw new InternalError("ouch") }

    // Chaining Method Responses
    // subscriber.receive(_) >>> ["ok", "fail", "ok"] >> { throw new InternalError() } >> "ok"


    // Stub

    def "should declare stub"() {

        when:
        Subscriber2 subscriber2 = Stub {
            receive("message1") >> "ok"
            receive("message2") >> "fail"
        }
        then:
        subscriber2.receive("message1") == "ok"
        subscriber2.receive("message2") == "fail"
    }

}