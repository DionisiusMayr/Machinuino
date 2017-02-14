/* Input */
const int clock = 5;
const int _inputButton = 6;

/* Output */
const int _led1 = 7;
const int _led2 = 8;

/* States */
const int __q1 = 0;
const int __q2 = 1;
const int __q3 = 2;
const int __q0 = 3;

bool isHigh(int pin) {
    return digitalRead(pin) == HIGH ? true : false;
}

int transition(int current) {
    switch(current) {
        case __q1:
            if (!isHigh(_inputButton)) return __q0;
            if (isHigh(_inputButton)) return __q2;
            break;
        case __q2:
            if (!isHigh(_inputButton)) return __q1;
            if (isHigh(_inputButton)) return __q3;
            break;
        case __q3:
            if (isHigh(_inputButton)) return __q0;
            if (!isHigh(_inputButton)) return __q2;
            break;
        case __q0:
            if (!isHigh(_inputButton)) return __q3;
            if (isHigh(_inputButton)) return __q1;
            break;
        default:
            // Not reachable
            exit(1);
            break;
    }
}

void output(int current) {
    switch(current) {
        case __q1:
            digitalWrite(_led1, LOW);
            digitalWrite(_led2, HIGH);
            break;
        case __q2:
            digitalWrite(_led2, LOW);
            digitalWrite(_led1, HIGH);
            break;
        case __q3:
            digitalWrite(_led1, HIGH);
            digitalWrite(_led2, HIGH);
            break;
        case __q0:
            digitalWrite(_led1, LOW);
            digitalWrite(_led2, LOW);
            break;
        default:
            // Not reachable
            exit(1);
            break;
    }
}

int currentState;

void setup() {
    /* Input */
    pinMode(clock, INPUT);
    pinMode(_inputButton, INPUT);

    /* Output */
    pinMode(_led1, OUTPUT);
    pinMode(_led2, OUTPUT);

    /* Initial state */
    currentState = __q0;
    output(__q0);
}

void loop() {
    while (digitalRead(clock) == LOW);

    currentState = transition(currentState);
    output(currentState);

    while (digitalRead(clock) == HIGH);
}
