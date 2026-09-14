const baseUrl = {
  local: "http://localhost:8080",
  test: "http://192.168.1.4:8080",
  prod: "https://api.xxx.com"
};
const currentBaseUrl = baseUrl.test;
export const request = async (options) => {
  const token = localStorage.getItem("token") || "";

  let url = currentBaseUrl + options.url;

  const config = {
    method: options.method || "GET",
    headers: {
      "Content-Type": "application/json",
    },
  };

  if (token) {
    config.headers.token = token;
  }

  const method = (options.method || "GET").toUpperCase();

  if (method === "GET" || method === "DELETE") {
    if (options.data && Object.keys(options.data).length > 0) {
      const params = new URLSearchParams();
      for (const key in options.data) {
        if (options.data[key] !== undefined && options.data[key] !== null && options.data[key] !== "") {
          params.append(key, options.data[key]);
        }
      }
      const queryString = params.toString();
      if (queryString) {
        url += `?${queryString}`;
      }
    }
  } else {
    if (options.data) {
      config.body = JSON.stringify(options.data);
    }
  }

  console.log(`请求: ${method} ${url}`, config.body ? JSON.parse(config.body) : '');

  try {
    const res = await fetch(url, config);

    // ====================== 关键修复：HTTP 401 处理 ======================
    if (res.status === 401) {
      // JWT 过期、未登录 → 清空信息并跳登录
      localStorage.removeItem("token");
      localStorage.removeItem("merchantId");
      window.location.href = "/login";
      return Promise.reject("登录已过期");
    }
    // ====================================================================

    const data = await res.json();
    console.log(`响应: ${method} ${url}`, data);

    return data;
  } catch (error) {
    console.error("请求失败:", error);
    throw error;
  }
};

// ========== API 接口 ==========

// 登录
export function onLogin(username, password) {
  return request({
    url: "/sys/user/login",
    method: "POST",
    data: { username, password },
  });
}

// 客户列表 - GET 请求
export function queryCustomerList(keyWord) {
  return request({
    url: "/crm/customer/list",
    method: "GET",
    data: keyWord ? { keyWord } : {},
  });
}

// 删除客户 - POST 请求
export function deleteCustomer(id) {
  return request({
    url: "/crm/customer/delete",
    method: "POST",
    data: { id },
  });
}

// 获取客户分类 - GET 请求
export function getCustomerTypeList() {
  return request({
    url: "/crm/customerType/list",
    method: "GET",
  });
}

// 保存客户 - POST 请求
export function saveCustomer(data) {
  return request({
    url: "/crm/customer/save",
    method: "POST",
    data: data,
  });
}

// 获取客户详情 - GET 请求
export function getCustomerInfo(id) {
  return request({
    url: `/crm/customer/info/${id}`,
    method: "GET",
  });
}

// 跟进列表 - GET 请求
export function getFollowList() {
  return request({
    url: "/crm/follow/list",
    method: "GET",
  });
}
// 待回访列表
export function getWaitFollowList() {
  return request({
    url: "/crm/follow/wait",
    method: "GET"
  });
}

// 保存跟进记录
export function saveFollow(data) {
  return request({
    url: "/crm/follow/save",
    method: "POST",
    data: data
  });
}

// 删除跟进记录
export function deleteFollow(id) {
  return request({
    url: "/crm/follow/delete/" + id,
    method: "POST"
  });
}
export function getFollowDetail(id) {
  return request({
    url: "/crm/follow/info/" + id,
    method: "GET"
  });
}
// 订单列表 - GET 请求
export function getOrderList() {
  return request({
    url: "/crm/order/page",
    method: "GET",
  });
}
export function getOrderInfo(id) {
  return request({
    url: "/crm/order/info/" + id,
    method: "GET"
  });
}
export function saveOrder(data) {
  return request({
    url: "/crm/order/save",
    method: "POST",
    data: data
  });
}

export function deleteOrder(id) {
  return request({
    url: "/crm/order/delete/" + id,
    method: "POST"
  });
}
// 订单列表 - GET 请求
export function getOrderPage(params) {
  return request({
    url: "/crm/order/page",
    method: "GET",
  });
}
// 首页统计 - GET 请求
export function getHomeCount() {
  return request({
    url: "/dashboard",
    method: "GET",
  });
}
export function getMyShop() {
  return request({
    url: "/crm/shop/my",
    method: "GET",
  });
}
// 保存/修改商铺
export function saveShop(data) {
  return request({
    url: "/crm/shop/save",
    method: "POST",
    data: data
  });
}
export function getEmployeeList() {
  return request({
    url: "/sys/user/employee/list",
    method: "GET",
  });
}
export function getGoodsList() {
  return request({ url: "/crm/goods/list", method: "GET" });
}
export function saveGoods(data) {
  return request({ url: "/crm/goods/save", method: "POST", data: data });
}
// 雇员列表分页
export function getEmpList(params) {
  return request({
    url: "/sys/user/employee/page",
    method: "GET",
    params: params
  })
}

// 保存/编辑雇员
export function saveEmp(data) {
  return request({
    url: "/sys/user/employee/save",
    method: "POST",
    data: data
  })
}

// 删除雇员
export function deleteEmp(id) {
  return request({
    url: "/sys/user/employee/delete/" + id,
    method: "POST"
  })
}
export function resetPwd(id) {
  return request({
    url: "/sys/user/resetPwd/" + id,
    method: "POST"
  });
}
export function getMyInfo() {
  return request({
    url: '/sys/user/info',
    method: 'get'
  })
}

// 修改我的信息 + 密码
export function updateMyInfo(data) {
  return request({
    url: '/sys/user/updatePwd',
    method: 'post',
    data
  })
}
