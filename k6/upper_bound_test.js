import { setupCodes, makeCall } from "./utils.js";

export const options = {
    thresholds: {
        http_req_failed: [{ threshold: 'rate<0.01', abortOnFail: true }],
        http_req_duration: [{ threshold: 'p(99)<50', abortOnFail: true }],
    },
    scenarios: {
        upper_bound: {
            executor: 'ramping-vus',
            stages: createStages(100)
        },
    }
}

function createStages(numStages) {
    const stages = []
    for (let i = 0; i < numStages; i++) {
        stages.push({
            duration: '30s',
            target: 25 * (i+1),
        })
    }
    return stages
}

export function setup() {
    return setupCodes()
}

export default function (data) {
    makeCall(data)
}