/* Pins */

/* Input */
const int clock = 10;
const int _button = 11;
const int _switch = 12;

/* Output */
const int _motor = 5;
const int _led = 6;

/* States */
const int __q0 = 0;
const int __q1 = 1;
const int __q2 = 2;
int currentState;
bool previousClock;

/* Setup */
void setup() {

    /* Input */
    pinMode(clock, INPUT);
    pinMode(_button, INPUT);
    pinMode(_switch, INPUT);

    /* Output */
    pinMode(_motor, OUTPUT);
    pinMode(_led, OUTPUT);

    /* Initial state */
    currentState = __q0;
    previousClock = false;
}

bool isHigh(int pin) {
    return digitalRead(pin) == HIGH ? true : false;
}

int transition(int current) {
    switch(current) {
        case __q0:
            if (isHigh(_button) && isHigh(_switch)) return __q1;
            if (isHigh(_button) && !isHigh(_switch)) return __q0;
            if (!isHigh(_button) && isHigh(_switch)) return __q2;
            if (!isHigh(_button) && !isHigh(_switch)) return __q1;
            break;
        case __q1:
            if (isHigh(_button) && isHigh(_switch)) return __q0;
            if (isHigh(_button) && !isHigh(_switch)) return __q0;
            if (!isHigh(_button) && isHigh(_switch)) return __q0;
            if (!isHigh(_button) && !isHigh(_switch)) return __q2;
            break;
        case __q2:
            if (isHigh(_button) && isHigh(_switch)) return __q1;
            if (isHigh(_button) && !isHigh(_switch)) return __q1;
            if (!isHigh(_button) && isHigh(_switch)) return __q2;
            if (!isHigh(_button) && !isHigh(_switch)) return __q2;
            break;
        default:
            // Not reachable
            exit(1);
            break;
    }
}

void output(int current) {
    switch(current) {
        case __q0:
            digitalWrite(_motor, HIGH);
            digitalWrite(_led, LOW);
            break;
        case __q1:
            digitalWrite(_motor, LOW);
            digitalWrite(_led, LOW);
            break;
        case __q2:
            digitalWrite(_motor, HIGH);
            digitalWrite(_led, HIGH);
            break;
        default:
            // Not reachable
            exit(1);
            break;
    }
}

/* Loop */
void loop() {

    /* Wait for clock */
    if (previousClock && digitalRead(clock) == HIGH) {
         currentState = transition(currentState);
         output(currentState);
    }

    previousClock = digitalRead(clock) == HIGH;
}
