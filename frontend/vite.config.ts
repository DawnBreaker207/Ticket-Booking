import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
    define: {
    global: 'window'
  },
  optimizeDeps: {
    include: ['sockjs-client']
  },
  plugins: [react(), tailwindcss()],
  server: {
    port: 4200 
  },
});
