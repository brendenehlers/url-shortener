import { setupCodes, makeCall } from "./utils.js";

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(99)<50'],
    },
    scenarios: {
        average_load: {
            executor: 'ramping-vus',
            stages: [
                { duration: '1m', target: 30 },
                { duration: '29m', target: 30 },
            ]
        },
    }
}

export function setup() {
    return setupCodes()
}

export default function (data) {
    makeCall(data)
}