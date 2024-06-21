import {defineConfig} from 'vite';
import vue from '@vitejs/plugin-vue';
import Components from 'unplugin-vue-components/vite';
import {AntDesignVueResolver} from 'unplugin-vue-components/resolvers';
import {resolve} from 'path';

export default defineConfig({
    server: {
        port: 5253,
        open: true
    },
    resolve: {
        alias: {
            '@': resolve(__dirname, './src') // 为./src配置别名
        }
    },
    plugins: [
        vue(),
        Components({
            resolvers: [
                AntDesignVueResolver({
                    importStyle: false // css in js
                })
            ]
        })
    ],
    build: {
        chunkSizeWarningLimit: 400,
        rollupOptions: {
            output: {
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        return id.substring(id.lastIndexOf('node_modules/') + 13).split('/')[0];
                    }
                },
                entryFileNames: 'assets/js/[name].[hash].js',
                chunkFileNames: 'assets/js/[name].[hash].js',
                assetFileNames: 'assets/[ext]/[name].[hash].[ext]'
            }
        }
    }
});
