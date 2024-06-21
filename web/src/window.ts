import {reactive} from 'vue';

export const WindowData = reactive({
    height: window.innerHeight,
    width: window.innerWidth,
});

window.onresize = () => {
    WindowData.height = window.innerHeight;
    WindowData.width = window.innerWidth;
};