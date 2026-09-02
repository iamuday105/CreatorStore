import axios from 'axios';
export const api=axios.create({baseURL:'http://localhost:8080/api',headers:{'Content-Type':'application/json'}});
export const productApi={getAll:()=>api.get('/products'),getById:id=>api.get(`/products/${id}`),create:data=>api.post('/products',data),update:(id,data)=>api.put(`/products/${id}`,data),remove:id=>api.delete(`/products/${id}`)};
export const orderApi={getAll:()=>api.get('/orders'),getById:id=>api.get(`/orders/${id}`),create:data=>api.post('/orders',data)};
