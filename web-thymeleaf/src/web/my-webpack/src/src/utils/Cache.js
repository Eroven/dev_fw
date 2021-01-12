
const tokenKey = "ACCESS_TOKEN"
const rememberMe = "REMEMBER_ME"

const ls = window.localStorage
const ss = window.sessionStorage

export default {
    setToken(token) {
        ss.setItem(tokenKey, token)
    },
    getToken() {
        return  ss.getItem(tokenKey) || ls.getItem(tokenKey) 
    },
    rememberToken(token) {
        ls.setItem(tokenKey, token)
    },
    cleanToken() {
        ss.removeItem(tokenKey)
        ls.removeItem(tokenKey)
    },
    rememberMe(rm) {
        if (true == rm) {
            ls.setItem(rememberMe, true)
        } else {
            ls.removeItem(rememberMe)
        }
    },
    isRememberMe() {
        return ls.getItem(rememberMe)
    }
}
