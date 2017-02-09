/* Input */
const int clock = 10;
const int _switch = 12;
const int _button = 11;

/* Output */
const int _motor = 5;
const int _led = 6;

/* States */
const int __q1 = 0;
const int __q2 = 1;
const int __q0 = 2;

bool isHigh(int pin) {
    return digitalRead(pin) == HIGH ? true : false;
}

int transition(int current) {
    switch(current) {
        case __q1:
            if (!isHigh(_switch) && isHigh(_button)) return __q0;
            if (!isHigh(_button) && isHigh(_switch)) return __q0;
            if (isHigh(_switch) && isHigh(_button)) return __q0;
            if (!isHigh(_button) && !isHigh(_switch)) return __q2;
            break;
        case __q2:
            if (!isHigh(_switch) && isHigh(_button)) return __q1;
            if (!isHigh(_button) && isHigh(_switch)) return __q2;
            if (isHigh(_switch) && isHigh(_button)) return __q1;
            if (!isHigh(_button) && !isHigh(_switch)) return __q2;
            break;
        case __q0:
            if (isHigh(_switch) && isHigh(_button)) return __q1;
            if (!isHigh(_switch) && isHigh(_button)) return __q0;
            if (!isHigh(_button) && isHigh(_switch)) return __q2;
            if (!isHigh(_button) && !isHigh(_switch)) return __q1;
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
            digitalWrite(_led, LOW);
            digitalWrite(_motor, LOW);
            break;
        case __q2:
            digitalWrite(_motor, HIGH);
            digitalWrite(_led, HIGH);
            break;
        case __q0:
            digitalWrite(_led, LOW);
            digitalWrite(_motor, HIGH);
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
    pinMode(_switch, INPUT);
    pinMode(_button, INPUT);

    /* Output */
    pinMode(_motor, OUTPUT);
    pinMode(_led, OUTPUT);

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
