import http from "k6/http";
import { check } from "k6";

export const SERVICE_HOST = "http://localhost:8080"

const redirectTargets = [
    "https://brendenehlers.com",
    "https://google.com",
    "https://facebook.com",
    "https://youtube.com",
    "https://amazon.com",
    "https://twitter.com",
    "https://wikipedia.org",
    "https://instagram.com",
    "https://linkedin.com",
    "https://reddit.com",
    "https://netflix.com"
]

export function setupCodes() {
    const createRedirectUrl = `${SERVICE_HOST}/api/v1/url`
    const shortCodes = []
    for (let url of redirectTargets) {
        const payload = JSON.stringify({
            longUrl: url
        })
        const params = {
            headers: {
                "Content-Type": "Application/json"
            }
        }
        const res = http.post(createRedirectUrl, payload, params)

        const statusCheckName = `response code for ${url} was 200`
        const shortCodeCheckName = `short code for ${url} is not null`
        const body = JSON.parse(res.body)
        check(res, {
            [statusCheckName]: (res) => res.status === 200,
            [shortCodeCheckName]: () => body.shortCode != null,
        })

        shortCodes.push(body.shortCode)
    }

    console.log("shortCodes: ", shortCodes)
    return {
        shortCodes
    }
}

export function makeCall(data) {
    const shortCodes = data.shortCodes
    const randomIndex = Math.floor(Math.random()*shortCodes.length)
    const requestUrl = `${SERVICE_HOST}/${shortCodes[randomIndex]}`
    const params = {
        redirects: 0
    }
    const res = http.get(requestUrl, params)

    check(res, {
        "Response should have 302 status": (res) => res.status === 302
    })
}