export type Poll = {
    id: number;
    question: string;
    description: string;
    startTime?: Date;
    endTime?: Date;
    timeLimit?: number;
    active: boolean;
    privateAccess: boolean;
    votes: Vote[];
    groups: Group[];
    users: User[];
    creator: User;
}

export type Vote = {
    id: number;
    choice: boolean;
    timestamp: Date;
    user: User;
    poll: Poll;
}

export type User = {
    id: number;
    username: string;
    password: string;
    role: string;
    createdPolls: Poll[];
    participatedPolls: Poll[];
    votes: Vote[];
    groups: Group[];
}


export type Group = {
    id: number;
    name: string;
    members: User[];
    polls: Poll[];
}
