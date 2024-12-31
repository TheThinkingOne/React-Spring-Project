// ajax 통신하는 함수 작성

import axios from "axios"

// 경로 설정
// api 서버 작업

export const API_SERVER_HOST = 'http://localhost:8080'

const prefix = `${API_SERVER_HOST}/api/todo`

// 비동기 통신
export const getOne = async (tno) => { // async = 비동기통신
    const res = await axios.get(`${prefix}/${tno}`)

    return res.data
}

export const getList = async (PageParam) => {

    const {page,size} = PageParam

    const res = await axios.get(`${prefix}/list`, {params:{page,size}})

    // async 의 모든 리턴값은 비동기이다.
    return res.data
}