import {createRouter, createWebHashHistory, Router} from 'vue-router';
import MainPage from '../views/MainPage.vue';
import LoginPage from '../views/LoginPage.vue';
import RegisterPage from '../views/RegisterPage.vue';

const router: Router = createRouter({
    history: createWebHashHistory('/web'),
    routes: [
        {
            path: '/',
            name: 'Main',
            component: MainPage
        },
        {
            path: '/login',
            name: 'Login',
            component: LoginPage
        },
        {
            path: '/register',
            name: 'Register',
            component: RegisterPage
        }
    ]
});

export default router;

export function goToLogin() {
    localStorage.clear();
    sessionStorage.clear();
    router.push({name: "Login"}).then(_ => undefined);
}

export function goToHome() {
    router.push({name: "Main"}).then(_ => undefined);
}