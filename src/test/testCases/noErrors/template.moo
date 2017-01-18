moore M1 {
    /* The first state is also the initial state */
    states {
        q0, q1, q2
    }

    input {
        /* The number is the pin number, and the name is the variable label in the target code */
        pins {
            clock:  10,     /* There must be a clock pin to control the readings */
            button: 11,
            switch: 12
        }

        /* The outer "q0" is the current state, the boolean expression is the symbol of the
        language and the state after the arrow is the next state. */
        transition {
            q0 {
                button  & switch  -> q1,
                button  & !switch -> q0,
                !button & switch  -> q2,
                !button & !switch -> q1
            }

            q1 {
                button  & switch  -> q0,
                button  & !switch -> q0,
                !button & switch  -> q0,
                !button & !switch -> q2
            }

            q2 {
                button  & switch  -> q1,
                button  & !switch -> q1,
                !button & switch  -> q2,
                !button & !switch -> q2
            }
        }
    }

    output {
        pins {
            motor: 5,
            led:   6
        }

        /* Dictates the output of each state. */
        function {
            q0 {
                motor,
                !led
            }

            q1 {
                !motor,
                !led
            }

            q2 {
                motor,
                led
            }
        }
    }
}
