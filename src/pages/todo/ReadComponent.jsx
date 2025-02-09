import React from 'react';
import { useEffect } from 'react';
import { useState } from 'react';
import { getOne } from '../../api/todoApi';
import useCustomMove from '../../hooks/useCustomMove';

const initState = {
    tno:0,
    title:'',
    writer: '',
    dueDate: '',
    complete: false
}

// React의 컴포넌트는 상태가 변경되면 자동으로 렌더링된다
//

function ReadComponent({tno}) {

    const [todo, setTodo] = useState(initState)

    const {moveToList, moveToModify} = useCustomMove()

    useEffect(() => {

        getOne(tno).then(data => {
            console.log(data)
            setTodo(data)
        })

        // 기동기 호출 막기
        // 번호가 바뀌어 상태가 바뀌면 다시 랜더링 되게 한다?
        // 무한 호출 방지?

    }, [tno]);

    return (
        <div className = "border-2 border-sky-200 mt-10 m-2 p-4">



            {makeDiv('Tno', todo.tno)}
            {makeDiv('Writer', todo.writer)}
            {makeDiv('Title', todo.title)}
            {makeDiv('Due Date', todo.dueDate)}
            {makeDiv('Complete', todo.complete ? 'Complete' : 'Not yet')}

            {/* button...... start  */}
            <div className="flex justify-end p-4">

                <button type="button"
                        className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
                        onClick={() => moveToList()}
                >
                    List
                </button>

                <button type="button"
                        className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
                        onClick={() => moveToModify(todo.tno)} // 해당 tno 에 해당하는 게시글 변경 페이지로 이동
                >
                    Modify
                </button>

            </div>
        </div>
    );
}

const makeDiv = (title, value) =>
    <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
            <div className="w-1/5 p-6 text-right font-bold">{title}</div>
            <div className="w-4/5 p-6 rounded-r border border-solid shadow-md">
                {value}
            </div>
        </div>
    </div>


export default ReadComponent;