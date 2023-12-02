package com.twitter.demo.auth

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("tokens")
class Token {
    @Id
    String id
    def value

    Token() {
    }

    Token(value) {
        this.value = value
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Token token = (Token) o

        if (id != token.id) return false
        if (value != token.value) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        return result
    }


    @Override
    String toString() {
        return "Token{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}'
    }
}
